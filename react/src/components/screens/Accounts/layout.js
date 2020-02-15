import React from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import i18next from 'i18next';
import PropTypes from 'prop-types';
import Loader from '../../Loader';
import Account from './components/Account';
import AccountPropType from '../../../proptypes/AccountPropType';
import WithError from '../../hocs/WithError';
import WithLoading from '../../hocs/WithLoading';
import SearchBar from './components/SearchBar';

const Accounts = ({ accounts, getUsers, hasMore, onSubmit, filters }) => {
    const inputStyle = "form-control filter-input mb-2";
    return (
        <div>
            <center>
                <h1>
                    {i18next.t('createUserForm.accounts')}
                </h1>
            </center>
            <div className="container">
                <SearchBar onSubmit={onSubmit} inputStyle={inputStyle} 
                            label={i18next.t('createUserForm.username')}
                            initialValues={filters} />
            </div>
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
    hasMore: PropTypes.bool.isRequired,
    filters: PropTypes.object.isRequired
}

export default WithError(WithLoading(Accounts));