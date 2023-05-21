/*
 *   Copyright (c) 2023 
 *   All rights reserved.
 */
const express = require('express');
const bodyParser = require('body-parser');

const app = express();
app.use(bodyParser.json());

function getRandomNumber(min, max, notDouble = false) {
  const randomNumber = Math.random() * (max - min) + min;
  if (notDouble) {
    return Number(randomNumber.toFixed(0));
  } else {
    return Number(randomNumber.toFixed(2));
  }
}

let targetFil = 0;
let targetDry = 0;
let targetDg = 0;
let targetAcidity = 0;
let targetDielectricLoss = 0;
let targetBreakdownVoltage = 0;
let targetAdd = 0;

let currentFil = 0;
let currentDry = 0;
let currentDg = 0;
let currentAcidity = 0;
let currentDielectricLoss = 0;
let currentBreakdownVoltage = 0;
let currentAdd = 0;

let currentState = 0;
let isRunning = false;
let workFinished = false;

app.post('/process', (req, res) => {
  const savedJob = req.body;
  // Генерация случайных чисел
  targetFil = getRandomNumber(2, 4, true);
  targetDry = getRandomNumber(8, 13);
  targetDg = getRandomNumber(6, 11);
  targetAcidity = getRandomNumber(20, 50);
  targetDielectricLoss = getRandomNumber(30, 50);
  targetBreakdownVoltage = getRandomNumber(500, 750);
  targetAdd = getRandomNumber(200, 450, true);

  currentFil = savedJob.cleanlinessClass;
  currentDry = savedJob.waterContent;
  currentDg = savedJob.gasContent;
  currentAcidity = savedJob.acidity;
  currentDielectricLoss = savedJob.dielectricLoss;
  currentBreakdownVoltage = savedJob.breakdownVoltage ;
  currentAdd = 0;

  const response = {
    targetFil,
    targetDry,
    targetDg,
    targetAcidity,
    targetDielectricLoss,
    targetBreakdownVoltage,
    targetAdd
  };

  res.json(response);
});


app.get('/getCurrent', (req, res) => {

  let currentStateReq = currentState.toFixed(0)
  let currentFilReq = currentFil.toFixed(0)
  let currentDryReq = currentDry.toFixed(2)
  let currentDgReq = currentDg.toFixed(2)
  let currentAcidityReq = currentAcidity.toFixed(2)
  let currentDielectricLossReq = currentDielectricLoss.toFixed(2)
  let currentBreakdownVoltageReq = currentBreakdownVoltage.toFixed(2)
  let currentAddReq = currentAdd.toFixed(2)
  
  const currentData = {
    currentStateReq,
    currentFilReq,
    currentDryReq,
    currentDgReq,
    currentAcidityReq,
    currentDielectricLossReq,
    currentBreakdownVoltageReq,
    currentAddReq,
    workFinished,
    isRunning
  };
  res.json(currentData);
});

app.post('/start', (req, res) => {
  if (!isRunning) {
    isRunning = true;
    currentState = 1;
    workFinished = false;
    runStateMachine();
    res.send('Starting the state machine...');
  } else {
    res.send('The state machine is already running.');
  }
});

app.post('/next', (req, res) => {
  isRunning = true;
  if(currentState!=5){
    currentState = currentState+1;
  }
});

app.post('/stop', (req, res) => {
  isRunning = false;
  currentState = 0;
  res.send('Stopping the state machine...');
});

app.listen(3000, () => {
  console.log('Server is listening on port 3000');
});

function runStateMachine() {
  if (!isRunning) {
    return;
  }
  console.log(currentState)

  switch (currentState) {
    case 1:
      currentFil += (targetFil - currentFil) * 0.1; 
      if (Math.abs(targetFil - currentFil) < 0.1) {
        currentState = 2;
      }
      break;

    case 2:
      currentDry += (targetDry - currentDry) * 0.1; 
      if (Math.abs(targetDry - currentDry) < 0.1) {
        currentState = 3;
      }
      break;

    case 3:
      currentDg += (targetDg - currentDg) * 0.1;
      if (Math.abs(targetDg - currentDg) < 0.1) {
        currentState = 4;
      }
      break;

    case 4:
      currentAcidity += (targetAcidity - currentAcidity) * 0.1; 
      currentDielectricLoss += (targetDielectricLoss - currentDielectricLoss) * 0.1; 
      currentBreakdownVoltage += (targetBreakdownVoltage - currentBreakdownVoltage) * 0.1;
      if (
        Math.abs(targetAcidity - currentAcidity) < 0.1 &&
        Math.abs(targetDielectricLoss - currentDielectricLoss) < 0.1 &&
        Math.abs(targetBreakdownVoltage - currentBreakdownVoltage) < 0.1
      ) {
        currentState = 5;
      }
      break;

    case 5:
      currentAdd += (targetAdd - currentAdd) * 0.1; // Example: Update by 10% each time
      if (
        Math.abs(targetAdd - currentAdd) < 0.1
      ) {
        workFinished = true
        isRunning = false
        console.log('machine process finished')
        return;
      }
      break;

    default:
      break;
  }

  setTimeout(runStateMachine, 1000);
}

