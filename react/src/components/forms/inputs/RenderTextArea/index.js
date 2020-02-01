import React from 'react';
import createRenderer from '../CreateRenderer';

const RenderTextArea = createRenderer((input, label, inputType, id, isDisabled, meta) => {
    return ( <textarea {...input} placeholder={label} id= {id} 
                        className="form-control" rows={3} maxLength={140}/>);
});

//TODO maybe add PropTypes

export default RenderTextArea;