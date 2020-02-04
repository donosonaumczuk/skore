import React from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import i18next from 'i18next';
import RenderTimePicker from '../../../inputs/RenderTimePicker';

const MatchTime = ({ updateTime }) => {
    return (
        <div className="form-group col-6">
            <div className="form-row">
                <div className="col-12">
                    <Field name="matchTime" label={i18next.t('createMatchForm.from')} 
                            updateTime={updateTime} component={RenderTimePicker} />
                </div>
            </div>
        </div>
    );
}

MatchTime.propTypes = {
    updateTime: PropTypes.func.isRequired
}

export default MatchTime;