// react
import React, {useState} from 'react'
import { Link as RouterLink } from 'react-router-dom'
// material-ui components
import Link from '@material-ui/core/Link'
import { render } from '@testing-library/react';

//
export const importAllImagesFromFolder = (r) => {
    let images = {};
    r.keys().map((item, index) => { images[item.replace('./', '').replace(/\.[^/.]+$/, "")] = r(item); });
    return images;
}

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

