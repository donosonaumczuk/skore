import React from 'react';
import PropTypes from 'prop-types';
import EditUserInfoForm from '../../forms/EditUserInfoForm';
import WithPermission from '../../hocs/WithPermission';
import WithError from '../../hocs/WithError';
import WithLoading from '../../hocs/WithLoading';

const EditUserInfo = ({ initialValues, username, history }) => 
      <EditUserInfoForm initialValues={initialValues} username={username} 
                                history={history} />

EditUserInfo.propTypes = {
    initialValues: PropTypes.object.isRequired,
    username: PropTypes.string.isRequired,
    history: PropTypes.object.isRequired
}

export default WithPermission(WithError(WithLoading(EditUserInfo)));