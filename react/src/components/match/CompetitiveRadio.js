import React from 'react';
import { Field } from 'redux-form';
import RadioInput from '../forms/inputs/RadioInput';

const CompetitiveRadio = () => {
    return  (
        <div className="form-group">
            <label htmlFor="competitiveness">
                competitive
                <span className="text-muted">*</span>
                <span className="tooltip-icon ml-2 far fa-question-circle" data-toggle="tooltip" 
                        data-html="true" data-placement="right" title="matchModeToolTip" />
            </label>
            <div className="input-group">
                <div className="btn-group btn-group-toggle" data-toggle="buttons" id="competitiveness">
                    <Field labelStyle="btn btn-green" id="friendly" name="competitivity" 
                            props={{ value: "friendly" }} radioValue="friendly" labelText="Friendly" component={RadioInput} />
                    <Field labelStyle="btn btn-green" id="competitive" name="competitivity"
                            props={{ value: "competitive" }} radioValue="competitive" labelText="Competitive" component={RadioInput} />
                </div>
            </div>
        </div>
    );
}

export default CompetitiveRadio;