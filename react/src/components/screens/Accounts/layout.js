import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import Loader from '../../Loader';
import Account from './components/Account';
import AccountPropType from '../../../proptypes/AccountPropType';

const Accounts = ({ accounts, getUsers, hasMore }) => {
    return (
        <div>
            <center>
                <h1>
                    {i18next.t('createUserForm.accounts')}
                </h1>
            </center>
            <div className="container">
                <InfiniteScroll dataLength={accounts.length} next={getUsers}
                                hasMore={hasMore} loader={<Loader />} >
                    {accounts.map((account, i) => <Account key={i} account={account} />)}
                </InfiniteScroll> 
            </div>
        </div>
    );
}

Accounts.propTypes = {
    accounts: PropTypes.arrayOf(AccountPropType).isRequired,
    getUsers: PropTypes.func.isRequired,
    hasMore: PropTypes.bool.isRequired
}

export default Accounts;