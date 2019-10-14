// import 'aframe';
// import './components/aframe-custom';
// import ReactDom from 'react'
// import h from 'preact';
import LidarPoints from './lidarPoints';
import CustomSelection from "./customSelection";
import MainScene from "./mainScene";
// var React = require('react');
// var ReactDOM = require('react-dom');
// var Main = require('./main');
var THREE = require('three');
var mainScene;
var scene;
var groupOfLines = new THREE.Group();
var groupOfPoints = new THREE.Group();
var groupOfCameras = new THREE.Group();

// document.addEventListener('DOMContentLoaded', () => {
//     ReactDOM.render(<Main/>, document.querySelector('#app'));
// });

document.addEventListener('DOMContentLoaded', () => {

    setUpScene();
    var customDrawing;
    // var customDrawing = new CustomDrawing();
    var customSelection = new CustomSelection({
        customDrawing: customDrawing,
        scene: mainScene,
        groupOfCameras: groupOfCameras,
        groupOfLines: groupOfLines
    });
    // window.addEventListener('resize', function () {
    //     document.getElementById("drawingCanvasDiv").innerHTML = '';
    //     customDrawing = new CustomDrawing();
    //     customSelection.customDrawing = customDrawing;
    // });
    new LidarPoints({
        stepNumber: 0,
        maxStepNumber: maxOfSlider,
        menuId: menuId,
        mainScene: mainScene
    });
    mainScene.animate();


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
        mainScene.camera.position.set(0, 0.4, 0.1);
    }
});

