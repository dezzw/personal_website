import { useEffect } from 'react';

import * as THREE from 'three';

import SceneInit from './SceneInit';

function Icon() {
  useEffect(() => {
    const test = new SceneInit('myThreeJsCanvas');
    test.initialize();
    test.animate();

    const boxGeometry = new THREE.BoxGeometry(16, 16, 16);
    const boxMaterial = new THREE.MeshNormalMaterial();
    const boxMesh = new THREE.Mesh(boxGeometry, boxMaterial);

    test.scene.add(boxMesh);
  }, []);

  return (
    <div className="icon rounded-base bg-white shadow">
      <canvas id="myThreeJsCanvas" className="rounded-base"/>
    </div>
  );
}

export default Icon;
