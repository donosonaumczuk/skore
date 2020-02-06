import React from 'react';
import WithLoading from '../../hocs/WithLoading';
import WithError from '../../hocs/WithError';

const CancelMatch = () => {
    return (
        <React.Fragment></React.Fragment>
    );
}

export default WithError(WithLoading(CancelMatch));