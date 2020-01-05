var THREE = require('three');
import CustomDragControls from "./customDragControls";
import CustomSelectionResizer from "./customSelectionResizer";
import CustomGuiItemNameSelection from "./customGuiItemNameSelection"

class CustomSelection {
    constructor(props) {
        this.isFrameStopped = false;
        this.isDraggingControlEnabled = false;
        this.selectionCounter = 0;


        this.mainScene = props.scene;
        this.scene = props.scene.scene;
        this.renderer = this.mainScene.renderer;
        this.mainCamera = this.mainScene.camera;
        this.dragControls = null;
        this.raycaster = new THREE.Raycaster();
        this.plane = new THREE.Plane();

        this.mouse = new THREE.Vector2();
        this.lineObjects = [];
        this.additionalCamerasObjects = [];
        this.material = new THREE.LineBasicMaterial({color: 0xff0000, linewidth: 4});
        this.groupOfCameras = props.groups.groupOfCameras;
        this.groupOfLines = props.groups.groupOfLines;
        this.rotationMatrix = new THREE.Matrix3();
        this.worldStartingDirection = new THREE.Vector3(0, 0, -1);


        this.currentMouse2DForDeletion = {x: null, y: null};
        this.deletionEvent = new MouseEvent("tryToDeleteSelectionEvent");
        this.customDrawing = props.customDrawing;
        this.customSelectionResizer = new CustomSelectionResizer(
            {
                customSelection: this
            }
        );
        this.customGuiItemNameSelectionModal = new CustomGuiItemNameSelection();
        this.scatchBoxCube = this.createScratchBoxCubeForStoppingScene();
        this.initEvents();
    }

    async initEvents() {
        var that = this;
        window.onkeydown = async function (e) {
            if (!e) e = window.event;
            //drawing
            if (e.ctrlKey) {
                if (!that.isFrameStopped) {
                    that.scatchBoxCube.visible = true;
                    // just for forcing renderer to check all objects in scene
                    that.mainScene.stopAnimation();
                    that.mainScene.animate();
                    that.mainScene.stopAnimation();

                    that.isFrameStopped = true;
                    that.customDrawing.showDrawingCanvas();
                }
            }
            // cancel drawing
            if (e.altKey) {
                if (that.isFrameStopped) {
                    that.scatchBoxCube.visible = false;
                    that.isFrameStopped = false;
                    that.mainScene.animate();
                    that.customDrawing.clearAndHideCanvas();
                }
            }
            // submit border
            if (e.shiftKey) {
                document.querySelector('#drawingCanvas').setAttribute('visible', 'false');
                if (that.isFrameStopped) {
                    if (that.customDrawing.endPointX != null && that.customDrawing.startPointX != null) {
                        Promise.all(that.createModalAndListenToEvents()).then(
                            data => {
                                let itemNameObject = data[0];
                                if (itemNameObject !== null && itemNameObject.length >= 1) {
                                    that.createItemNameInDatabaseReturnPromise(itemNameObject)
                                        .then(function (selectedItemNameFromDB) {
                                            that.makeBorder(selectedItemNameFromDB);
                                            that.customGuiItemNameSelectionModal.addNewItemNameToSelection(selectedItemNameFromDB);
                                            that.scatchBoxCube.visible = false;
                                            that.isFrameStopped = false;
                                            that.mainScene.animate();
                                            that.customDrawing.clearAndHideCanvas();
                                        });
                                } else {
                                    that.scatchBoxCube.visible = false;
                                    that.isFrameStopped = false;
                                    that.mainScene.animate();
                                    that.customDrawing.clearAndHideCanvas();
                                }
                            }
                        );
                    }
                }
            }
            // moving selections 'M'
            if (e.keyCodeVal === 77 || ( e.key && e.key.toLocaleLowerCase() === 'M'.toLocaleLowerCase())) {
                if (that.dragControls) {
                    if (that.isDraggingControlEnabled) {
                        that.dragControls.setCursorToAuto();
                        that.dragControls.deactivate();
                        that.isDraggingControlEnabled = false;
                    } else {
                        that.dragControls.activate();
                        that.isDraggingControlEnabled = true;
                    }
                }
            }
            //deletion of selection 'DEL'
            if (e.keyCodeVal === 46 || ( e.key && e.key.toLocaleLowerCase() === 'Delete'.toLocaleLowerCase())) {
                if (that.dragControls) {
                    if (that.isDraggingControlEnabled && false === that.isFrameStopped) {
                        document.dispatchEvent(that.deletionEvent);
                    } else {
                        alert("To delete selection please enable dragging (press key 'M')");
                    }
                }
            }
            //for resizing key 'N'
            if (e.keyCodeVal === 78 || ( e.key && e.key.toLocaleLowerCase() === 'N'.toLocaleLowerCase())) {
                if (that.customSelectionResizer) {
                    if (that.customSelectionResizer.enabled && false === that.isFrameStopped) {
                        that.customSelectionResizer.disable();
                    } else {
                        that.customSelectionResizer.enable();
                    }
                }
            }

        };
        document.addEventListener("mousemove", function (e) {
            that.currentMouse2DForDeletion.x = e.pageX;
            that.currentMouse2DForDeletion.y = e.pageY;
        });
        document.addEventListener("tryToDeleteSelectionEvent", function (e) {
            let mouse2D = new THREE.Vector2();
            mouse2D.x = (that.currentMouse2DForDeletion.x / window.innerWidth) * 2 - 1;
            mouse2D.y = -(that.currentMouse2DForDeletion.y / window.innerHeight) * 2 + 1;

            that.raycaster.setFromCamera(mouse2D, that.mainCamera);
            var intersects = that.raycaster.intersectObjects(that.lineObjects);
            if (intersects.length > 0) {
                let selection = intersects[0].object;
                if (selection.userData.camera.parent) {
                    selection.userData.camera.parent.remove(selection.userData.camera);
                    selection.parent.remove(selection);
                    selection.geometry.dispose();
                    selection.material.dispose();
                    selection = undefined;
                }
            }
        });
    }


    createModalAndListenToEvents() {
        return [this.customGuiItemNameSelectionModal.showModalWithWaitingForActions()];
    }

    makeBorder(selectedItemNameFromDB) {

        let firstPoint;
        let secondPoint;

        this.customDrawing.restructureSoStartIsInLeftTopCorner();

        firstPoint = this.create3DPoint(this.customDrawing.startPointX, this.customDrawing.startPointY);
        secondPoint = this.createSecondPointFromPlane(this.customDrawing.endPointX, this.customDrawing.endPointY, firstPoint);


        let xLength;
        let yLength;

        if (firstPoint.y < secondPoint.y) {
            yLength = new THREE.Vector3(firstPoint.x, secondPoint.y, firstPoint.z).length();
            xLength = new THREE.Vector3(secondPoint.x, firstPoint.y, secondPoint.z).length();
        } else {
            yLength = -(firstPoint.distanceTo(new THREE.Vector3(firstPoint.x, secondPoint.y, firstPoint.z)));
            xLength = firstPoint.distanceTo(new THREE.Vector3(secondPoint.x, firstPoint.y, secondPoint.z));
        }
        // yLength = -(firstPoint.distanceTo(new THREE.Vector3(firstPoint.x, secondPoint.y, firstPoint.z)));
        // xLength = firstPoint.distanceTo(new THREE.Vector3(secondPoint.x, firstPoint.y, secondPoint.z));


        let line = this.create3DLine(xLength, yLength);
        let reultVec = firstPoint.clone().add(secondPoint).divideScalar(2).setLength(3);

        let camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 1, 100);
        camera.position.set(0, mainCameraYPosition, 0);
        camera.lookAt(reultVec);
        camera.userData.cameraHashUuID = this.uuidv4();
        this.groupOfCameras.add(camera);
        this.additionalCamerasObjects.push(camera);
        let worldRotation = new THREE.Quaternion();
        camera.getWorldQuaternion(worldRotation);

        line.position.set(reultVec.x, reultVec.y, reultVec.z);
        line.rotation.setFromQuaternion(worldRotation);

        line.renderOrder = 1;
        line.userData = {
            camera: camera,
            xLength: xLength,
            yLength: yLength,
            name: '_Selection_' + this.selectionCounter,
            rotation: line.rotation,
            selectedItemNameObject: selectedItemNameFromDB
        };
        this.selectionCounter += 1;
        this.groupOfLines.add(line);
        this.lineObjects.push(line);


        if (this.dragControls) {
            this.dragControls.dispose();
        }

        this.dragControls = new CustomDragControls(this.lineObjects, this.mainCamera, this.renderer.domElement);
        this.isDraggingControlEnabled = false;
        this.dragControls.deactivate();
    }

    uuidv4() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }

    create3DLine(xLength, yLength) {

        return new THREE.Line(this.createLineGeometry(xLength, yLength), this.material);
    }

    createLineGeometry(xLength, yLength) {
        let rectShape = new THREE.Shape();

        rectShape.moveTo(-xLength / 2, yLength / 2);
        rectShape.lineTo(xLength / 2, yLength / 2);
        rectShape.lineTo(xLength / 2, -yLength / 2);
        rectShape.lineTo(-xLength / 2, -yLength / 2);
        rectShape.lineTo(-xLength / 2, yLength / 2);
        rectShape.moveTo(0, 0);

        return new THREE.ShapeBufferGeometry(rectShape);
    }

    createSecondPointFromPlane(screenX, screenY, firstpoint) {
        this.mouse.x = (screenX / window.innerWidth) * 2 - 1;
        this.mouse.y = -(screenY / window.innerHeight) * 2 + 1;
        let result = new THREE.Vector3();

        this.raycaster.setFromCamera(this.mouse, this.mainCamera);
        this.plane.setFromNormalAndCoplanarPoint(this.mainCamera.getWorldDirection(this.plane.normal), firstpoint);
        this.raycaster.ray.intersectPlane(this.plane, result);
        return result;
    }

    create3DPoint(screenX, screenY) {

        let x = (screenX / window.innerWidth) * 2 - 1;
        let y = -(screenY / window.innerHeight) * 2 + 1;
        let cameraYOffset = mainCameraYPosition;

        let vNow = new THREE.Vector3(x, y, 0);
        vNow.unproject(this.mainCamera);

        let length = Math.sqrt(vNow.x ** 2 + (vNow.y - cameraYOffset) ** 2 + vNow.z ** 2);
        let scalingFactor = 3 / Math.abs(length);
        return new THREE.Vector3((scalingFactor * vNow.x), ((scalingFactor * (vNow.y - cameraYOffset)) + cameraYOffset), (scalingFactor * vNow.z));
    }

    createItemNameInDatabaseReturnPromise(itemName) {
        return new Promise(function (resolve, reject) {
            let request = new XMLHttpRequest();
            request.open('POST', '/selectedItem/save/name', true);
            request.onreadystatechange = function () {
                if (request.readyState === 4) {
                    if (request.status === 200) {
                        resolve(JSON.parse(request.response));
                    } else {
                        reject();
                    }
                }
            };
            request.send(itemName);
        });
    }

    createScratchBoxCubeForStoppingScene() {
        const material = new THREE.MeshPhongMaterial({
            color: 0xF3F3F3,
            opacity: 0.5,
            transparent: true,
            side: THREE.DoubleSide
        });
        let geometry = new THREE.BoxBufferGeometry(2, 2, 2);
        const cube = new THREE.Mesh(geometry, material);
        cube.position.set(0, mainCameraYPosition, 0);
        this.scene.add(cube);
        cube.visible = false;
        return cube;
    }
}

export default CustomSelection;