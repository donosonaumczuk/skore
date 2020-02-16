import React from 'react';
import renderer from 'react-test-renderer';
import SportPlayers from '../components/SportPlayers';

test('SportPlayers snapshot test', () => {
    //set up
    const playersQuantity = 2;
    
    //execution
    const component = renderer.create(
      <SportPlayers playersQuantity={playersQuantity} />,
    );
    let tree = component.toJSON();

    //postconditions
    expect(tree).toMatchSnapshot();
});