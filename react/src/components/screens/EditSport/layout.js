import React from 'react';
import PropTypes from 'prop-types';
import EditSportForm from '../../forms/EditSportForm';

const EditSport = ({ initialValues, history }) => 
    <EditSportForm initialValues={initialValues} history={history} />

EditSport.propTypes = {
    initialValues: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired
}

export default EditSport;