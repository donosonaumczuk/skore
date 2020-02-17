import React from 'react';
import PropTypes from 'prop-types';
import EditSportForm from '../../forms/EditSportForm';
import WithLoading from '../../hocs/WithLoading';
import WithError from '../../hocs/WithError';
import WithPermission from '../../hocs/WithPermission';

const EditSport = ({ initialValues, history, sportPlayerQuantity }) =>
    <EditSportForm initialValues={initialValues} history={history}
                    sportPlayerQuantity={sportPlayerQuantity} />

EditSport.propTypes = {
    initialValues: PropTypes.object.isRequired,
    history: PropTypes.object.isRequired,
    sportPlayerQuantity: PropTypes.number.isRequired
}

export default WithPermission(WithError(WithLoading(EditSport)));
