import React from 'react';
import RadioInput from '../forms/inputs/RadioInput';

const CompetitiveRadio = () => {
    return  (
        <div class="form-group">
            <label for="competitiveness">competitive<span className="text-muted">*</span>
                <span class="tooltip-icon ml-2 far fa-question-circle" data-toggle="tooltip" data-html="true" data-placement="right" title="matchModeToolTip"/>
            </label>
            <div className="input-group">
                <div className="btn-group btn-group-toggle" data-toggle="buttons" id="competitiveness">
                    <RadioInput labelStyle="btn btn-green" id="friendly" value="Friendly" labelText="Friendly" />
                    <RadioInput labelStyle="btn btn-green" id="competitive" value="Competitive" labelText="Competitive" />
                </div>
            </div>
        </div>
    );
}

export default CompetitiveRadio;