import { useEffect } from 'react';

import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';

import SceneInit from '../lib/SceneInit';

function Icon() {
  useEffect(() => {
    // const scene = new SceneInit('myThreeJsCanvas');
    // scene.initialize();
    // let animate = scene.animate();

    // const loader = new GLTFLoader();
		
    // loader.load( 'src/assets/thing.glb', ( gltf ) => {
    //   gltf.scene.traverse(c => {
    //     c.castShadow = true;
    //   });		
    //   scene.scene.add( gltf.scene );
			
    // }, undefined, function ( error ) {
			
    //   console.error( error );

    // } );

    // return () => { 
    //   window.cancelAnimationFrame(animate);
    // }
  }, []);

  return (
    <div className="icon rounded-base bg-white shadow">

    </div>
  );
}

export default Icon;
