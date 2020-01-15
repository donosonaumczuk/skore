import React from 'react';
import CreateMatchPoster from './CreateMatchPoster';

const LeftPanel = () => {
    return (
        <div className="container-fluid">
            <div className="row">
                <CreateMatchPoster />
            </div>
        </div>
    );
}

export default LeftPanel;