import React from 'react';
import createRenderer from './CreateRenderer';

const RenderInput = createRenderer((input, label, inputType, isDisabled, meta) => {
  if (isDisabled) {
    return (<input {...input} type={inputType} placeholder={label} className="form-control" disabled/>);
  }
  else {
    return (<input {...input} type={inputType} placeholder={label} className="form-control" />);
  }
});

export default RenderInput;