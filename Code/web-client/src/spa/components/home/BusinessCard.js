// react
import React from 'react';
// material-ui components
import Avatar from '@material-ui/core/Avatar'
import Card from '@material-ui/core/Card'
import CardContent from '@material-ui/core/CardContent'
import CardHeader from '@material-ui/core/CardHeader'
import Icon from '@material-ui/core/Icon'
import Link from '@material-ui/core/Link'
import LinkedIn  from '@material-ui/icons/LinkedIn'
import Typography from '@material-ui/core/Typography'
import { makeStyles } from '@material-ui/core/styles'

const useStyles = makeStyles((theme) => ({
  layout: {
    width: 370,
    height: 200,
    textAlign:'left',
  },
  avatar: {
    width: 70,
    height: 70,
  }
}));

export default function BussinessCard(props) {

  const classes = useStyles();
  const [onOver, setOnMouseOver] = React.useState(2);

  const handleOnMouseOver = () => {
    setOnMouseOver(10);
  };
  const handleOnMouseOut = () => {
    setOnMouseOver(2);
  };

  return (
    <Link href={props.href} style={{textDecoration:'none'}}>
        <Card className={classes.layout}
        elevation={onOver}
        onMouseOver={handleOnMouseOver}
        onMouseOut={handleOnMouseOut}>
            <CardHeader
            avatar={
              <Avatar className={classes.avatar} alt={props.name} src={props.avatar} />
            }
            action={
              <Icon color="primary">
                  <LinkedIn />
              </Icon>
            }
            title={props.name}
            subheader={props.email}
            />
            <CardContent>
                <Typography variant="body2" color="textSecondary" component="p">
                    {props.text}
                </Typography>
            </CardContent>
        </Card>
    </Link>
  );
}
