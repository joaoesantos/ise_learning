import unittest

def addDigits(num: int) -> int:
    digital_root = 0
    while num > 0:
        digital_root += num % 10
        num = num // 10
        
        if num == 0 and digital_root > 9:
            num = digital_root
            digital_root = 0
            
    return digital_root

class TestChallenge(unittest.TestCase):
    def test(self):
        self.assertEqual(addDigits(38), 2)
        self.assertEqual(addDigits(10), 1)
        self.assertEqual(addDigits(44), 8)

if __name__ == '__main__':
    unittest.main()