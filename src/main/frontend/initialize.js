import LidarPoints from './lidarPoints';
import CustomSelection from "./customSelection";
import MainScene from "./mainScene";
import CustomDrawing from "./customDrawing";

var THREE = require('three');
var mainScene;
var scene;
var groupOfLines = new THREE.Group();
var groupOfPoints = new THREE.Group();
var groupOfCameras = new THREE.Group();


document.addEventListener('DOMContentLoaded', () => {

    setUpScene();
    // var customDrawing;
    var customDrawing = new CustomDrawing();
    var customSelection = new CustomSelection({
        customDrawing: customDrawing,
        scene: mainScene,
        groups: {
            groupOfCameras,
            groupOfLines
        }
    });
    var lidarPoints = new LidarPoints({
        stepNumber: 0,
        maxStepNumber: maxOfSlider,
        menuId: menuId,
        isReplay: false,
        mainScene: mainScene,
        groups: {
            groupOfCameras,
            groupOfLines,
            groupOfPoints
        }
    });
    lidarPoints.render();
    mainScene.animate();



    window.addEventListener('resize', function () {
        document.getElementById("drawingCanvasDiv").innerHTML = '';
        customDrawing = new CustomDrawing();
        customSelection.customDrawing = customDrawing;
    });

    function setUpScene() {
        // scene = document.querySelector('a-scene');
        mainScene = new MainScene();
        scene = mainScene.scene;
        groupOfLines.name = 'groupOfLines';
        groupOfPoints.name = 'groupOfPoints';
        groupOfCameras.name = 'groupOfCameras';
        scene.add(groupOfLines);
        scene.add(groupOfPoints);
        scene.add(groupOfCameras);
        mainScene.camera.position.set(0, mainCameraYPosition, 0.000000000000001);

    }
});

