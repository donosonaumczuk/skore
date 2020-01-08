import React from 'react';
import createRenderer from './CreateRenderer';

const RenderInput = createRenderer((input, label) =>
  <input {...input} placeholder={label} />
)

export default RenderInput;