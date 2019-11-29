import {CustomFrustum} from "./customFrustum";
import CustomDatGuiMenu from "./customDatGuiMenu";

var THREE = require('three');

class LidarPoints {
    constructor(props) {
        this.mainScene = props.mainScene;
        this.state = {
            stepNumber: props.stepNumber,
            isReplay: props.isReplay,
            menuId: props.menuId,
        };
        this.createDatGuiUI(props.maxStepNumber);
        this.texHolder = new THREE.TextureLoader();
        this.groups = props.groups;
    }

    removeSpheres() {
        let scenel = this.mainScene.scene;
        let selectedObject = scenel.getObjectByName("groupOfPoints");
        if (selectedObject) {
            selectedObject.children.forEach(function (x) {
                x.geometry.dispose();
                x.material.dispose();
            });
            selectedObject.children = [];
        }
        if (this.state.isReplay) {
            let lines = scenel.getObjectByName("groupOfLines");
            if (selectedObject) {
                lines.children.forEach(function (x) {
                    x.geometry.dispose();
                    x.material.dispose();
                });
                lines.children = [];
            }
        }
    }

    makeBackroundIMGWithLoadDataCallBack(callBack) {
        let allPromises = [];
        let allImagePath = [];
        let frontPicture = {path: '/rawData/image/front/' + menuId + '/' + this.state.stepNumber, index: 0};
        let backPicture = {path: '/rawData/image/back/' + menuId + '/' + this.state.stepNumber, index: 1};
        allImagePath.push(frontPicture);
        allImagePath.push(backPicture);

        let that = this;
        for (let imagePath of allImagePath) {
            allPromises.push(new Promise(function (resolve, reject) {
                that.texHolder.load(imagePath['path'], (texture => {
                    resolve(texture);
                }))
            }));
        }
        Promise.all(allPromises)
            .then( async function (arrayOfMaterials) {
                that.mainScene.frontSphere.material.map = arrayOfMaterials[frontPicture.index];
                that.mainScene.frontSphere.material.needsUpdate = true;
                that.mainScene.backSphere.material.map = arrayOfMaterials[backPicture.index];
                that.mainScene.backSphere.material.needsUpdate = true;
                await that.takePicturesFromCameras();
                callBack();
            }, function (error) {
                console.error("Could not load all textures:", error);
            });
    }

    takePicturesFromCameras() {
        let scene = this.mainScene.scene;
        let renderer = this.mainScene.renderer;

        if (renderer == null) {
            return;
        }

        if (this.groups.groupOfCameras) {
            if (this.groups.groupOfCameras.children.length > 0) {
                this.createImageFromCameras(scene, renderer);
            }
        }
    }

    async createImageFromCameras(scene, renderer) {
        let dataToSend = [];
        let numberOfLines = this.groups.groupOfLines.children.length;
        for (let i = 0; i < numberOfLines; i++) {
            let camera = this.groups.groupOfLines.children[i].userData.camera;
            renderer.render(scene, camera);
            let dataURL = renderer.domElement.toDataURL();
            dataToSend.push({
                cameraHashUuID: camera.userData.cameraHashUuID,
                pictureInBase64: dataURL
            });
        }
        let imageDataDtos = await this.sendImagesToServerAndWaitForResultWithImageId(JSON.stringify(dataToSend));
        let imageDataDtosLength = imageDataDtos.length;
        for (let j = 0; j < numberOfLines; j++) {
            let line = this.groups.groupOfLines.children[j];
            let cameraHash = line.userData.camera.userData.cameraHashUuID;
            for (let k = 0; k < imageDataDtosLength; k++){
                if (imageDataDtos[k]["cameraHashFromFrontEnd"] === cameraHash){
                    line.userData.newlySavedPictureId = imageDataDtos[k]["newlySavedPictureId"];
                    console.log("just set " + imageDataDtos[k]["newlySavedPictureId"]);
                    break;
                }
            }
        }

    }
    // async sleep(msec) {
    //     return new Promise(resolve => setTimeout(resolve, msec));
    // }

    sendImagesToServerAndWaitForResultWithImageId(dataToSend) {
        return new Promise(function (resolve, reject) {
            const http = new XMLHttpRequest();
            http.open('POST', '/selectedItem/save/camera-images', false);
            http.setRequestHeader('Content-type', 'application/json');
            http.onreadystatechange = function () {
                if (http.readyState === 4 && http.status === 200) {
                    console.log(http.responseText);
                    //todo fix this data doesnt wait for await
                    resolve( JSON.parse(http.response));
                }
            };
            http.send(dataToSend); // Make sure to stringify
        });
    }

    sendImagesToServer(dataToSend) {
        const http = new XMLHttpRequest();
        http.open('POST', '/selectedItem/save/camera-images');
        http.setRequestHeader('Content-type', 'application/json');
        http.send(dataToSend); // Make sure to stringify
    }

    loadDataFromServerAndRenderPoints() {
        let that = this;
        let request = new XMLHttpRequest();
        if (!this.state.isReplay) {
            // request.open('GET', '/dataStored/' + this.state.stepNumber, true);
            request.open('GET', '/rawData/' + this.state.menuId + '/' + this.state.stepNumber, true);
        } else {
            request.open('GET', '/dataStoredReplay/' + this.state.stepNumber, true);
        }

        request.setRequestHeader('Content-Type', 'application/json');
        request.onreadystatechange = function () {
            if (request.readyState === 4 && request.status === 200) {
                that.state.lidarPoints = JSON.parse(request.response);
                that.renderPointsFromData();
                that.hideLoadingModal();
                if (!that.state.isReplay && that.groups.groupOfCameras.children.length !== 0) {
                    that.getAndSendSelectedDataToBackend();
                }
            }
        };
        request.send(null);
    }

    renderPointsFromData() {
        let spheres = this.groups.groupOfPoints;

        if (!spheres) {
            return;
        }

        let geometry = new THREE.SphereBufferGeometry(0.005, 5, 5);
        geometry.verticesNeedUpdate = true;
        let material = new THREE.MeshLambertMaterial({color: 0x39ff14});

        let that = this;
        let thatPoints = this.state.lidarPoints;
        if (that.state.isReplay) {
            thatPoints = JSON.parse(thatPoints);
        }

        for (let index in thatPoints) {
            if (that.state.isReplay) {
                let current = thatPoints[index];
                for (let innerOne in current) {
                    let value = current[innerOne];
                    if (innerOne.toLowerCase().indexOf("line") >= 0) {
                        that.createBorderAndRotate(value);
                        continue;
                    }
                    let sphere = new THREE.Mesh(geometry, material);
                    sphere.dynamic = true;
                    sphere.verticesNeedUpdate = true;
                    sphere.position.set(value.x, value.y, value.z);
                    spheres.add(sphere);
                }
                continue;
            }
            let value = thatPoints[index];
            let sphere = new THREE.Mesh(geometry, material);
            sphere.dynamic = true;
            sphere.verticesNeedUpdate = true;
            sphere.position.set(value[0], value[2], -value[1]);
            spheres.add(sphere);
        }
    }

    createBorderAndRotate(line) {
        let lines = this.mainScene.scene.getObjectByName("groupOfLines");
        let borderLine = this.create3DLineBorder(line.xLength, line.yLength);
        borderLine.position.set(line.position.x, line.position.y, line.position.z);
        borderLine.rotation.set(line.rotationEuler._x, line.rotationEuler._y, line.rotationEuler._z);
        lines.add(borderLine);

    }

    create3DLineBorder(xLength, yLength) {
        var rectShape = new THREE.Shape();
        rectShape.moveTo(-xLength / 2, yLength / 2);
        rectShape.lineTo(xLength / 2, yLength / 2);
        rectShape.lineTo(xLength / 2, -yLength / 2);
        rectShape.lineTo(-xLength / 2, -yLength / 2);
        rectShape.lineTo(-xLength / 2, yLength / 2);
        rectShape.moveTo(0, 0);

        let geometry = new THREE.ShapeBufferGeometry(rectShape);
        let material = new THREE.LineBasicMaterial({color: 0xff00ff, linewidth: 2});
        return new THREE.Line(geometry, material);
    }

    showLoadingModal() {
        (function () {
            document.querySelector('#loadModal').hidden = false;
        })();
    }

    hideLoadingModal() {
        (function () {
            document.querySelector('#loadModal').hidden = true;
        })();
    }

    getAndSendSelectedDataToBackend() {
        const http = new XMLHttpRequest();
        http.open('POST', '/selectedItem/save/data-part');
        http.setRequestHeader('Content-type', 'application/json');
        http.send(JSON.stringify(this.createFrustumForShapeAndGetData()));
    }

    createFrustumForShapeAndGetData() {
        let spheres = this.groups.groupOfPoints;
        let lines = this.groups.groupOfLines;
        let cameraPosition = new THREE.Vector3(0, 0.4, 0);
        let vec1, vec2, vec3, vec4, vec5, vec6, vec7, vec8, vec9, vec10, vec11, vec12;
        var plane1, plane2, plane3, plane4;
        let result = [];

        if (!lines) {
            return;
        }
        let that = this;
        if (lines.children.length > 0) {
            let index = 0;
            lines.children.forEach(function (line) {

                //left
                vec1 = that.rotateVectorAndReturnPosition(new THREE.Vector3(-line.userData.xLength / 2, line.userData.yLength / 2, 0), line);
                vec2 = that.rotateVectorAndReturnPosition(new THREE.Vector3(-line.userData.xLength / 2, -line.userData.yLength / 2, 0), line);
                vec3 = cameraPosition.clone();
                plane1 = new THREE.Plane();
                plane1.setFromCoplanarPoints(vec1, vec2, vec3);
                let rotationMatrix = that.createRotationMatrix4AroudYAxis(Math.PI);
                plane1.applyMatrix4(rotationMatrix);
                plane1.normal.negate();
                //bottom
                vec4 = vec2.clone();
                vec5 = that.rotateVectorAndReturnPosition(new THREE.Vector3(line.userData.xLength / 2, -line.userData.yLength / 2, 0), line);
                vec6 = cameraPosition.clone();
                plane2 = new THREE.Plane();
                plane2.setFromCoplanarPoints(vec4, vec5, vec6);

                //right
                vec7 = vec5.clone();
                vec8 = that.rotateVectorAndReturnPosition(new THREE.Vector3(line.userData.xLength / 2, line.userData.yLength / 2, 0), line);
                vec9 = cameraPosition.clone();
                plane3 = new THREE.Plane();
                plane3.setFromCoplanarPoints(vec7, vec8, vec9);

                //top
                vec10 = vec1.clone();
                vec11 = vec8.clone();
                vec12 = cameraPosition.clone();
                plane4 = new THREE.Plane();
                plane4.setFromCoplanarPoints(vec10, vec11, vec12);
                plane4.normal.negate();

                // more like pyramid than frustum. Two planes are missing to be frustum
                let frustum = new CustomFrustum(plane1, plane2, plane3, plane4);

                //add line to result
                let lineForRecreation = {
                    position: line.position,
                    rotationEuler: line.rotation,
                    xLength: line.userData.xLength,
                    yLength: line.userData.yLength
                };
                let eachResult = {};
                eachResult.line = lineForRecreation;
                let selectedSpheresData = {};
                spheres.children.forEach(function (sphere) {
                    if (frustum.containsPoint(sphere.position)) {
                        selectedSpheresData[index] = [sphere.position.x, sphere.position.y, sphere.position.z];
                        index++;
                    }
                });
                eachResult.selectedItemSpheresData = selectedSpheresData;

                let selectedDataRequestDto = {
                    menuId: menuId,
                    stepNumber: that.state.stepNumber,
                    selectedItemNameObject: line.userData.selectedItemNameObject,
                    rawSelectedDataWithLine: JSON.stringify(eachResult),
                    pictureId: line.userData.newlySavedPictureId
                };
                result.push(selectedDataRequestDto);

            });
        }
        return result;

    }

    rotateVectorAndReturnPosition(vector, line) {
        let positionOfRotationCenter = line.position.clone();
        vector.applyEuler(line.rotation.clone());
        return positionOfRotationCenter.add(vector);
    }

    createRotationMatrix4AroudYAxis(angleInRad) {
        let rotationMatrix4 = new THREE.Matrix4();
        rotationMatrix4.set(
            Math.cos(angleInRad), 0, Math.sin(angleInRad), 0,
            0, 1, 0, 0,
            -Math.sin(angleInRad), 0, Math.cos(angleInRad), 0,
            0, 0, 0, 1
        );
        return rotationMatrix4;
    }

    render() {
        this.showLoadingModal();
        this.removeSpheres();
        //callback to take picture of selection
        this.makeBackroundIMGWithLoadDataCallBack(this.loadDataFromServerAndRenderPoints.bind(this));
        // //callbacks for rendering, frustum etc
        // this.loadDataFromServerAndRenderPoints();
    }

    createDatGuiUI(maxStepNumber) {
        new CustomDatGuiMenu(this, maxStepNumber).createDatGuiSliderAndActionMenu();
    }
}

export default (LidarPoints)