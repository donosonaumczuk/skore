import React from 'react';
import Proptypes from 'prop-types';

const UserData = ({ styleClass, value, tag }) => {
    const data = !tag ? value : tag;
    return (
        <div className="row text-center">
            <div className="col">
                <p className={styleClass}>{data}</p>
            </div>
        </div>
    );
}
 
UserData.propTypes = {
    styleClass: Proptypes.string.isRequired,
    value: Proptypes.string,
    tag: Proptypes.object
}

export default UserData;
