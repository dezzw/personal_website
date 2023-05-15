import { useEffect } from 'react';

import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';

import SceneInit from '../lib/SceneInit';

function Icon() {
  useEffect(() => {
    const scene = new SceneInit('myThreeJsCanvas');
    scene.initialize();
    scene.animate();

    const loader = new GLTFLoader();
		
    loader.load( 'src/assets/thing.glb', ( gltf ) => {
      gltf.scene.traverse(c => {
        c.castShadow = true;
      });		
      scene.scene.add( gltf.scene );
			
    }, undefined, function ( error ) {
			
      console.error( error );

    } );
  }, []);

  return (
    <div className="icon rounded-base bg-white shadow">
      <canvas id="myThreeJsCanvas" className="rounded-base"/>
    </div>
  );
}

export default Icon;
