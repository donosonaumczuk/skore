import React from 'react';
import createRenderer from './CreateRenderer';

const RenderInput = createRenderer((input, label, inputType, id, isDisabled, meta) => {
  if (isDisabled) {
    return (<input {...input} type={inputType} placeholder={label} id= {id}
                                         className="form-control" disabled/>);
  }
  else {
    return (<input {...input} type={inputType} placeholder={label} id={id}
                                                 className="form-control" />);
  }
});

export default RenderInput;