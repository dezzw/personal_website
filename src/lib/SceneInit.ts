import * as THREE from 'three';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';

export default class SceneInit {

  scene: THREE.Scene;
  camera: THREE.PerspectiveCamera;
  renderer: THREE.WebGLRenderer;
  clock: THREE.Clock;
  controls: OrbitControls;
  ambientLight: THREE.AmbientLight;
	directionalLight: THREE.DirectionalLight;

  constructor(canvasId: string) {
    // NOTE: Core components to initialize Three.js app.
    this.scene = new THREE.Scene();
    this.camera = new THREE.PerspectiveCamera(
      45, 1, 1, 1000
    );
    this.renderer = new THREE.WebGLRenderer(
      {
	canvas: document.getElementById(canvasId) as HTMLCanvasElement,
	antialias: true
      }
    );
		
    this.clock = new THREE.Clock();
    this.controls = new OrbitControls(this.camera, this.renderer.domElement);

    // NOTE: Lighting is basically required.
    this.ambientLight = new THREE.AmbientLight(0x101010, 4.0);
    this.directionalLight = new THREE.DirectionalLight(0xFFFFFF, 1.0);
  }

  initialize() {
    this.camera.position.z = 48;
    this.renderer.setSize(285, 285);
    this.renderer.setClearColor(0xffffff, 0)
		
    this.scene.add(this.ambientLight);

	  this.directionalLight.position.set(20, 100, 10);
    this.directionalLight.target.position.set(0, 0, 0);
    this.directionalLight.castShadow = true;
    this.directionalLight.shadow.bias = -0.001;
    this.directionalLight.shadow.mapSize.width = 2048;
    this.directionalLight.shadow.mapSize.height = 2048;
    this.directionalLight.shadow.camera.near = 0.1;
    this.directionalLight.shadow.camera.far = 500.0;
    this.directionalLight.shadow.camera.near = 0.5;
    this.directionalLight.shadow.camera.far = 500.0;
    this.directionalLight.shadow.camera.left = 100;
    this.directionalLight.shadow.camera.right = -100;
    this.directionalLight.shadow.camera.top = 100;
    this.directionalLight.shadow.camera.bottom = -100;
    this.scene.add(this.directionalLight);


    // directional light - parallel sun rays
    // this.directionalLight = new THREE.DirectionalLight(0xffffff, 1);
    // // this.directionalLight.castShadow = true;
    // this.directionalLight.position.set(0, 32, 64);
    // this.scene.add(this.directionalLight);

    // if window resizes
    window.addEventListener('resize', () => this.onWindowResize(), false);
  }

  animate() {
    // NOTE: Window is implied.
    // requestAnimationFrame(this.animate.bind(this));
    this.controls.update();
    this.render();
    window.requestAnimationFrame(this.animate.bind(this));
  }

  render() {
    // NOTE: Update uniform data on each render.
    // this.uniforms.u_time.value += this.clock.getDelta();
    this.renderer.render(this.scene, this.camera);
  }

  onWindowResize() {
    this.camera.aspect = 1.0;
    this.camera.updateProjectionMatrix();
    this.renderer.setSize(285, 285);
  }
}
