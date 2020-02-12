import React from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import i18next from 'i18next';
import RenderTimePicker from '../../../inputs/RenderTimePicker';

const MatchTime = ({ changeFieldsValue, touchField }) => {
    return (
        <div className="form-group col-6">
            <div className="form-row">
                <div className="col-12">
                    <Field name="matchTime" label={i18next.t('createMatchForm.from')} 
                            changeFieldsValue={changeFieldsValue}
                            touchField={touchField}
                            component={RenderTimePicker} />
                </div>
            </div>
        </div>
    );
}

MatchTime.propTypes = {
    changeFieldsValue: PropTypes.func.isRequired,
    touchField: PropTypes.func.isRequired
}

export default MatchTime;