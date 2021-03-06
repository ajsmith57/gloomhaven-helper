export const pushMonster = function(monster) {
  return {type: 'ADD_MONSTER', monster: monster}
};

export const popMonster = function(monster) {
  return {type: 'DELETE_MONSTER', monster: monster}
};

export const replaceMonster = function(monster) {
  return {type: 'UPDATE_MONSTER', monster: monster};
};

export const setScenario = function(val) {
  return {type: 'CHANGE_SCENARIO', value: val}
};

export const addError = function(err) {
  return {type: 'ADD_ERROR', error: err}
};

export const setRoom = function(data) {
  return {type: 'SET_ROOM', value: data}
};

export const setSession = function(data) {
  return {type: 'GET_SESSION', value: data}
};

export const setStatus = function(status) {
  return {type: 'SET_STATUS', value: status}
};

export const setShowModal = function(status) {
  return {type: 'SHOW_MODAL', value: status}
};

export const setHideModal = function(status) {
  return {type: 'HIDE_MODAL', value: status}
};

export const clearSession = function() {
  return {type: 'CLEAR_SESSION'};
};