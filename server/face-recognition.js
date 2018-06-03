const cv = require('opencv4nodejs');
const path = require('path');
const fs = require('fs');

const basePath = './';
const imgsPath = path.resolve(basePath, 'bigbangtheory-origin');
const nameMappings = ['Leonard', 'Penny', 'Rajesh', 'Sheldon'];
const classifier = new cv.CascadeClassifier(cv.HAAR_FRONTALFACE_ALT2);

const eigen = new cv.EigenFaceRecognizer();
const fisher = new cv.FisherFaceRecognizer();
const lbph = new cv.LBPHFaceRecognizer();
let i=0;
const getFaceImage = (grayImg) => {
    const faceRects = classifier.detectMultiScale(grayImg).objects;
    if(!faceRects.length){
        throw new Error('failed to detect faces');
    }
    return grayImg.getRegion(faceRects[0]);
}

const imgFiles = fs.readdirSync(imgsPath);


const images = imgFiles
    .map(file => path.resolve(imgsPath, file))
    .map(filePath => cv.imread(filePath))
    .map(img => img.bgrToGray())
    .map(getFaceImage)
    .map(faceImg => faceImg.resize(80, 80));
    // .map(outputImg => {
    //     // cv.imwrite('output/'+i, outputImg);
    //     cv.imshowWait('face detection', outputImg);
    // })

const isImageFour = (_, i) => imgFiles[i].includes('4');
const isNotImageFour = (_, i) => !isImageFour(_, i);

const trainImages = images.filter(isNotImageFour);
const testImages = images.filter(isImageFour);

const labels = imgFiles
    .filter(isNotImageFour)
    .map(file => nameMappings.findIndex(name => file.includes(name)));


eigen.train(trainImages, labels);
fisher.train(trainImages, labels);
lbph.train(trainImages, labels);

const runPrediction = (recognizer) => {
    testImages.forEach((img) => {
      const result = recognizer.predict(img);
      console.log('predicted: %s, confidence: %s', nameMappings[result.label], result.confidence);
      cv.imshowWait('face', img);
      cv.destroyAllWindows();
    });
  };
  
  console.log('eigen:');
  runPrediction(eigen);
  
  console.log('fisher:');
  runPrediction(fisher);
  
  console.log('lbph:');
  runPrediction(lbph);
