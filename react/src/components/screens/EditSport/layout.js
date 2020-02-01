import React from 'react';
import PropTypes from 'prop-types';
import EditSportForm from '../../forms/EditSportForm';

const EditSport = ({ formInitialValues, history }) => 
    <EditSportForm initialValues={formInitialValues} history={history} />

EditSport.propTypes = {
    formInitialValues: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired
}

export default EditSport;