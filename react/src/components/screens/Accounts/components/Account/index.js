import React from 'react';
import { Link } from 'react-router-dom';
import AccountPropType from '../../../../../proptypes/AccountPropType';

const Account = ({ account }) => {
    return (
        <Link className="name-label-link" to={`/users/${account.username}`}>
            <div className="card">
                <div className="card-body">
                    <h5 className="card-title">
                        {account.username}
                    </h5>
                </div>
            </div>
        </Link>
      );
}

Account.propTypes = {
    account: AccountPropType.isRequired
}

export default Account;