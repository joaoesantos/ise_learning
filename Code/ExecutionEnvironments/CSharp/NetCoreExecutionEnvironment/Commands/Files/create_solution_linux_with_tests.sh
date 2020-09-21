exec > /dev/null 2>&1
base_dir=$1
template_dir=$2
new_dir=$3

mkdir $base_dir/$new_dir

cp -avr  $base_dir/$template_dir/* $base_dir/$new_dir

exit