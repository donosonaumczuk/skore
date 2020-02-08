import React from 'react';
import Message from '../../Message';

function WithMessage(Component) {
    return function addMessage({ message, ...props }) {
        if (message) {
            return (
                <div>
                    <Message message={message} />
                    <Component {...props} />
                </div>
            );
        }
        else {
            return <Component {...props} />
        }
    };
}

export default WithMessage;