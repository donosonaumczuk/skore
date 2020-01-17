import React from 'react';
import i18next from 'i18next';
import Proptypes from 'prop-types';

const FilterTitle = ({ title, titleStyle }) => {
    return (
        <div className="row">
            <p className={titleStyle}>
                {title}
                <span className="tooltip-icon ml-2 far fa-question-circle" data-toggle="tooltip"
                    data-html="true" data-placement="right" title={i18next.t('home.filtersToolTip')} />
            </p>
        </div>
    );
}

FilterTitle.propTypes = {
    title: Proptypes.string.isRequired,
    titleStyle: Proptypes.string.isRequired
}

export default FilterTitle;