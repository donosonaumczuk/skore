import React from 'react';
import AnonymousForm from '../../forms/AnonymousForm';

function WithAnonymous(Component) {
    return function Anonymous({ anonymous, currentMatch, ...props }) {
        return anonymous ? <AnonymousForm currentMatch={currentMatch} /> :
                            <Component {...props} />;
    };
}

export default WithAnonymous;