import React from 'react';
import Proptypes from 'prop-types';

const Account = ({ account }) => {
    return (
        <div className="card">
            <div className="card-body">
                <h5 className="card-title">
                    {account.username}
                </h5>
                <h6 className="card-subtitle mb-2 text-muted">
                    {account.index}
                </h6>
            </div>
        </div>
      );
}

Account.propTypes = {
    account: Proptypes.object.isRequired //TODO replace with custom proptype
}

export default Account;