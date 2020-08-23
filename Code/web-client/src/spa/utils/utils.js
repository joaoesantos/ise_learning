
export function reduceObjectArrayToMap(array, keyForKey, keyForValue) {
    return array.reduce((accum, el) => {
        accum[el[keyForKey]] = el[keyForValue];
        return accum;
    } , {})
}