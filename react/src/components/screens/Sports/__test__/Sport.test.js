import React from 'react';
import renderer from 'react-test-renderer';
import Sport from '../components/Sport';

test('Sport snapshot test', () => {
    //set up
    const sport = {
        sportName: "SportName",
        displayName: "Display name",
        playerQuantity: 1,
        links: [
            {
                rel: "image",
                "href": "sportImageUrl"
            }
        ]
    }

    //execution
    const component = renderer.create(
      <Sport sport={sport} />,
    );
    let tree = component.toJSON();

    //postconditions
    expect(tree).toMatchSnapshot();
});