import React from 'react';
import AccountPropType from '../../../../../proptypes/AccountPropType';

const Account = ({ account }) => {
    return (
        <div className="card">
            <div className="card-body">
                <h5 className="card-title">
                    {account.username}
                </h5>
            </div>
        </div>
      );
}

Account.propTypes = {
    account: AccountPropType.isRequired
}

export default Account;