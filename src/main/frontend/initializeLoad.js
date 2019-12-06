import LidarPoints from "./lidarPoints";
import MainScene from "./mainScene";

var THREE = require('three');
var mainScene;
var scene;
var groupOfLines = new THREE.Group();
var groupOfPoints = new THREE.Group();

document.addEventListener('DOMContentLoaded', () => {

    setUpScene();
    var lidarPoints = new LidarPoints({
        stepNumber: 0,
        maxStepNumber: maxOfSlider,
        menuId: menuId,
        isReplay: true,
        mainScene: mainScene,
        groups: {
            groupOfLines,
            groupOfPoints
        }
    });
    lidarPoints.render();
    mainScene.animate();


    function setUpScene() {
        // scene = document.querySelector('a-scene');
        mainScene = new MainScene();
        scene = mainScene.scene;
        groupOfLines.name = 'groupOfLines';
        groupOfPoints.name = 'groupOfPoints';
        scene.add(groupOfLines);
        scene.add(groupOfPoints);
        mainScene.camera.position.set(0, mainCameraYPosition, 0.000000000000001);

    }
});

