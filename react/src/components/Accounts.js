import React from 'react';
import Proptypes from 'prop-types';
import i18next from 'i18next';

    const Accounts = ( {account} ) => {
      return (
        <div>
          <center><h1>{i18next.t('createUserForm.accounts')}</h1></center>
            <div className="card">
              <div className="card-body">
                <h5 className="card-title">{account.username}</h5>
                <h6 className="card-subtitle mb-2 text-muted">{account.email}</h6>
              </div>
            </div>
        </div>
      )
    };

    Accounts.propTypes = {
      account: Proptypes.object.isRequired //TODO replace with custom proptype
    }

    export default Accounts;
