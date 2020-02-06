import { validImageFormats } from '../ImageValidators';

//validImageFormats tests
test('validImageFormat with valid formats', () => {
    //set up
    const imageFormat1 = "image/png";
    const imageFormat2 = "image/jpeg";
    const imageFormat3 = "image/jpg";

    //execution
    const actualResult1 = validImageFormats(imageFormat1);
    const actualResult2 = validImageFormats(imageFormat2);
    const actualResult3 = validImageFormats(imageFormat3);

    //postconditions
    expect(actualResult1).toBeTruthy();
    expect(actualResult2).toBeTruthy();
    expect(actualResult3).toBeTruthy();
})

test('validImageFormat with invalid formats', () => {
    //set up
    const imageFormat = "image/svg";
    
    //execution
    const actualResult = validImageFormats(imageFormat);

    //postconditions
    expect(actualResult).toBeFalsy();
})