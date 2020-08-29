
export function reduceObjectArrayToMap(array, keyForKey, keyForValue, keyForIdValue) {
    return array.reduce((accum, el) => {
        accum[el[keyForKey]] = {
            value: el[keyForValue]
        }
        if(keyForIdValue && el[keyForIdValue]) {
            accum[el[keyForKey]].id = el[keyForIdValue];
        }
        return accum;
    } , {})
}