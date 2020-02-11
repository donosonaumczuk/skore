import React from 'react';
import renderer from 'react-test-renderer';
import { shallow } from 'enzyme';
import Sports from '../layout';

test('Sports snapshot test', () => {
    //set up
    const sports = [
        {
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
    ];
    const getSports = () => "sport function";
    const hasMore = false;

    //execution
    const component = renderer.create(
      <Sports sports={sports} getSports={getSports} hasMore={hasMore} />,
    );
    let tree = component.toJSON();

    //postconditions
    expect(tree).toMatchSnapshot();
});

test('enzyme demo', () => {
    const sports = [
        {
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
    ];
    const wrapper = shallow(<Sports sports={sports} />);
    expect(wrapper.exists()).toBeTruthy();
})