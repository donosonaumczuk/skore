import React from 'react';
import loader from './../../img/loader.gif';

const Loader = () => {
    return (
        //TODO center sometimes, maybe receive a parameter to center or with fix position
        <div className="row p-2 mt-2" id="loader">
          <div className="offset-5 col-2">
            <img className="img-fluid" src={loader} alt="loader" />
          </div>
        </div>
    );
}

export default Loader;