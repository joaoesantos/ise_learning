import unittest

def isNumberPalindrome(number: int) -> bool:
    if number < 0: 
        return  False
   
    original = number
    revertedNumber = 0

    while number != 0:
        remainder = number % 10
        revertedNumber = revertedNumber * 10 + remainder
        number //= 10

    return original == revertedNumber


class TestChallenge(unittest.TestCase):
    def test(self):
        self.assertEqual(isNumberPalindrome(121), True)
        self.assertEqual(isNumberPalindrome(10), False)
        self.assertEqual(isNumberPalindrome(44), True)
        self.assertEqual(isNumberPalindrome(107), False)

if __name__ == '__main__':
    unittest.main()