import React from 'react';

const createRenderer = render => ({ input, meta, label, inputType, required, ...rest }) => 
 
 <div className="form-group" >
    {/* className={[
      meta.error && meta.touched ? 'error' : '',
      meta.active ? 'active' : ''
    ].join(' ')} //TODO replace class we want to style error afte meta.error && meta.touchedr*/}
    <label>
      {label}
      <span className="text-muted">{required ? " *" : ""}</span>
    </label>
    <div>
      {render(input, label, inputType, rest)}
      {meta.error &&
        meta.touched &&
        <span>
          {meta.error}
        </span>}
    </div>
  </div>

export default createRenderer;
