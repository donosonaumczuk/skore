import React from 'react';
import createRenderer from '../CreateRenderer';
import PropTypes from 'prop-types';

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

RenderInput.propTypes = {
  input: PropTypes.object.isRequired,
  label: PropTypes.string.isRequired,
  inputType: PropTypes.string.isRequired,
  id: PropTypes.string,
  isDisabled: PropTypes.bool,
  meta: PropTypes.object.isRequired
}

export default RenderInput;