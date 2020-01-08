import React from 'react';

const createRenderer = render => ({ input, meta, label, ...rest }) => 
 
 <div >
    {/* className={[
      meta.error && meta.touched ? 'error' : '',
      meta.active ? 'active' : ''
    ].join(' ')} //TODO replace class we want to style error afte meta.error && meta.touchedr*/}
    <label>
      {label}
    </label>
    <div>
      {render(input, label, rest)}
      {meta.error &&
        meta.touched &&
        <span>
          {meta.error}
        </span>}
      </div>
  </div>


export default createRenderer;
