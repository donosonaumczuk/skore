import React from 'react';
import PropTypes from 'prop-types';
import EditUserInfoForm from '../../forms/EditUserInfoForm';

const EditUserInfo = ({ initialValues, username, history }) => 
      <EditUserInfoForm initialValues={initialValues} username={username} 
                                history={history} />

EditUserInfo.propTypes = {
    initialValues: PropTypes.object.isRequired,
    username: PropTypes.string.isRequired,
    history: PropTypes.object.isRequired
}

export default EditUserInfo;