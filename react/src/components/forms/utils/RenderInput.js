import React from 'react';
import createRenderer from './CreateRenderer';

const RenderInput = createRenderer((input, label, inputType) => {
  return (
    <input {...input} type={inputType} placeholder={label} className="form-control" />)});


export default RenderInput;