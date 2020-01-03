import {OrbitControls} from "three/examples/jsm/controls/OrbitControls";

var THREE = require('three');


class MainScene {
    constructor() {
        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
        this.renderer = new THREE.WebGLRenderer();
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        this.renderer.setClearColor(0xEEEEEE);
        this.createBasicControls();
        document.body.appendChild(this.renderer.domElement);
        window.addEventListener('resize', this.onWindowResize.bind(this), false);
        this.backSphere = this.createImageSphere(0);
        this.frontSphere = this.createImageSphere(Math.PI);
        this.scene.add(this.backSphere);
        this.scene.add(this.frontSphere);
    }

    animate() {
        this.frameId = requestAnimationFrame(this.animate.bind(this));
        this.renderer.render(this.scene, this.camera);
    }

    stopAnimation() {
        cancelAnimationFrame(this.frameId);
    }

    onWindowResize() {
        this.camera.aspect = window.innerWidth / window.innerHeight;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize(window.innerWidth, window.innerHeight);
    }

    createBasicControls() {
        this.controls = new OrbitControls(this.camera, this.renderer.domElement);
        this.controls.enableZoom = false;
        this.controls.enablePan = false;
        this.controls.target.y = mainCameraYPosition;
    }

    createImageSphere(phiStart) {
        let geometry = new THREE.SphereBufferGeometry(15, 32, 32, phiStart, Math.PI, 0, Math.PI);
        let material = new THREE.MeshBasicMaterial();
        material.side = THREE.DoubleSide;
        return new THREE.Mesh(geometry, material);
    }
}

export default (MainScene)