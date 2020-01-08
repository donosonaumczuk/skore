import React from 'react';

const createRenderer = render => ({ input, meta, label, ...rest }) => 
 
 <div >
    {/* className={[
      meta.error && meta.touched ? 'error' : '',
      meta.active ? 'active' : ''
    ].join(' ')} add class we want to style error*/}
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
