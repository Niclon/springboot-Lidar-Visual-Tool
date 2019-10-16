import {OrbitControls} from "three/examples/jsm/controls/OrbitControls";

var THREE = require('three');


class MainScene {
    constructor() {
        this.scene = new THREE.Scene();
        this.camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.001, 1000);
        this.renderer = new THREE.WebGLRenderer();
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        this.renderer.setClearColor(0xEEEEEE);
        this.createBasicControls();
        document.body.appendChild(this.renderer.domElement);
        window.addEventListener( 'resize', this.onWindowResize.bind(this), false );

        //todo remove
        var geometry = new THREE.BoxGeometry( 1, 1, 1 );
        var material = new THREE.MeshBasicMaterial( { color: 0x00ff00 } );
        var cube = new THREE.Mesh( geometry, material );
        cube.position.z = -4;
        this.scene.add( cube );
    }
    animate() {
        this.frameId = requestAnimationFrame( this.animate.bind(this) );
        this.renderer.render( this.scene, this.camera );
    }
    stopAnimation(){
        cancelAnimationFrame( this.frameId );
    }

    onWindowResize() {
        this.camera.aspect = window.innerWidth / window.innerHeight;
        this.camera.updateProjectionMatrix();
        this.renderer.setSize( window.innerWidth, window.innerHeight );
    }

    createBasicControls(){
        this.controls = new OrbitControls( this.camera, this.renderer.domElement );
        this.controls.enableZoom = false;
        this.controls.enablePan = false;
        this.controls.target.y = mainCameraYPosition;
    }
}

export default (MainScene)