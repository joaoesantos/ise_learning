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